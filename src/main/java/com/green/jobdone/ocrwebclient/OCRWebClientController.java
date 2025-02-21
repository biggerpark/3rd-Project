package com.green.jobdone.ocrwebclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jobdone.ocrwebclient.model.OCRWebClientDto;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@RequestMapping("/ocr")
public class OCRWebClientController {

    @Value("${upstage.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.upstage.ai/v1/document-ai/ocr")
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱을 위한 ObjectMapper

    @PostMapping("/upload")
    public Mono<OCRWebClientDto> uploadOCR(@RequestPart("file") MultipartFile file) {
        return Mono.fromCallable(() -> {
                    MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
                    bodyBuilder.part("document", file.getResource());
                    bodyBuilder.part("schema", "oac");
                    bodyBuilder.part("model", "ocr-2.2.1");

                    return webClient.post()
                            .header("Authorization", "Bearer " + apiKey)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .bodyValue(bodyBuilder.build())
                            .retrieve()
                            .bodyToMono(String.class);
                })
                .flatMap(responseMono -> responseMono) // Mono<Mono<String>> -> Mono<String>
                .map(response -> (OCRWebClientDto) processResponse(response)); // JSON 데이터 가공
    }

    private OCRWebClientDto processResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            // 특정 필드 추출 예제 (OCR 결과에서 "text" 필드만 반환)
            String result2 = jsonNode.path("text").asText();
            String result = result2.replaceAll(" ","").replaceAll("\n","");
            OCRWebClientDto ocrWebClientDto = new OCRWebClientDto();
            int startIdxName = result.indexOf("상호:");
            ocrWebClientDto.setBusinessName(result.substring(startIdxName + ("상호:").length(), result.indexOf("성명:", startIdxName)));
            int startIdxBusiCreatedAt = result.indexOf("개업연월일:");
//            String busiCreatedAt = result.substring(startIdxBusiCreatedAt + ("개업연월일:").length(), result.indexOf("생년월일", startIdxBusiCreatedAt));
            try {
                String busiCreatedAt = result.substring(
                        startIdxBusiCreatedAt + ("개업연월일:").length(),
                        result.indexOf("생년월일", startIdxBusiCreatedAt)
                );
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy년MM월dd일");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = inputFormat.parse(busiCreatedAt);
                ocrWebClientDto.setBusiCreatedAt(outputFormat.format(date));
            } catch (Exception e1) {
                try {
                    String busiCreatedAt = result.substring(
                            startIdxBusiCreatedAt + ("개업연월일:").length(),
                            result.indexOf("사업장", startIdxBusiCreatedAt)
                    );
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy년MM월dd일");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = inputFormat.parse(busiCreatedAt);
                    ocrWebClientDto.setBusiCreatedAt(outputFormat.format(date));
                } catch (Exception e2) {
                    String busiCreatedAt = "추출 실패"; // 예외 발생 시 기본값 설정
                }
            }

            int startIdxAddress = result.indexOf("소재지:");
            ocrWebClientDto.setAddress(result.substring(startIdxAddress + ("소재지:").length(),result.indexOf("사업의")));
            return ocrWebClientDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/upload2")
    public Mono<String> uploadOCR2(@RequestPart("file") MultipartFile file) {
        return Mono.fromCallable(() -> {
                    MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
                    bodyBuilder.part("document", file.getResource());
                    bodyBuilder.part("schema", "oac");
                    bodyBuilder.part("model", "ocr-2.2.1");

                    return webClient.post()
                            .header("Authorization", "Bearer " + apiKey)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .bodyValue(bodyBuilder.build())
                            .retrieve()
                            .bodyToMono(String.class);
                })
                .flatMap(responseMono -> responseMono) // Mono<Mono<String>> -> Mono<String>
                .map(response -> processResponse2(response)); // JSON 데이터 가공
    }

    private String processResponse2(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            // 특정 필드 추출 예제 (OCR 결과에서 "text" 필드만 반환)
            String result = jsonNode.path("text").asText();
            String result2 = result.replaceAll(" ","").replaceAll("\n","");
            return result2;
        } catch (JsonProcessingException e) {
            return "Invalid JSON Response";
        }
    }

}