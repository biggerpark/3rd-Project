package com.green.jobdone.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.UUID;

@Slf4j
@Component //빈등록
public class MyFileUtils {
    private final String uploadPath;

    public String getUploadPath() {
        return uploadPath;
    }

    /*
        @Value("${file.directory}")은
        yaml 파일에 있는 file.directory 속성에 저장된 값을 생성자 호출할 때 값을 넣어준다.
         */
    public MyFileUtils(@Value("${file.directory}") String uploadPath) {
        log.info("MyFileUtils - 생성자: {}", uploadPath);
        this.uploadPath = uploadPath;
    }

    public boolean folderExists(String folderPath) {
        File folder = new File(folderPath);
        return folder.exists() && folder.isDirectory();  // 폴더가 존재하고, 디렉토리일 경우 true 반환
    }

    // path = "ddd/aaa"
    // D:/2024-02/download/greengram_ver1/
    // feed/2
    // D:/2024-02/download/greengram_ver1/feed/2
    // D:/2024-02/download/greengram_ver1/ddd/aaa
    //디렉토리 생성
    public void makeFolders(String path) {
        File file = new File(uploadPath, path);
        // static 아님  >>  객체화하고 주소값.(file.)으로 호출했기 때문에
        // 리턴타입은 boolean  >>  if()안에서 호출했기 때문에
        // 파라미터는 없음   >>  호출 때 인자를 보내지 않았기 때문에
        // 메소드명은  >>  exists였다.
        if(!file.exists()) {
            file.mkdirs();
        }
    }

    //파일명에서 확장자 추출
    public String getExt(String fileName) {
        int lastIdx = fileName.lastIndexOf(".");
        return fileName.substring(lastIdx);
    }

    //랜덤파일명 생성
    public String makeRandomFileName() {
        return UUID.randomUUID().toString();
    }

    //랜덤파일명 + 확장자 생성하여 리턴
    public String makeRandomFileName(String originalFileName) {
        return makeRandomFileName() + getExt(originalFileName);
    }

    public String makeRandomFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        return makeRandomFileName(originalFileName);
    }

    //파일을 원하는 경로에 저장
    public void transferTo(MultipartFile mf, String path) throws IOException {
        File file = new File(uploadPath, path);
        mf.transferTo(file);
    }

    //폴더 삭제, e.g. "user/1"
    public void deleteFolder(String path, boolean deleteRootFolder) {
        File folder = new File(uploadPath,path); // 절대경오 박아야 폴더 없어져요 여러분
        if(folder.exists() && folder.isDirectory()) { //폴더가 존재하면서 디렉토리인가?
            File[] includedFiles = folder.listFiles();

            for(File f : includedFiles) {
                if(f.isDirectory()) {
                    deleteFolder(f.getAbsolutePath(), true);
                } else {
                    f.delete();
                }
            }

            if(deleteRootFolder) {
                folder.delete();
            }
        }

    }

    public boolean deleteFile(String path) {
        File file = new File(uploadPath, path);  // 경로와 파일명으로 File 객체 생성
        if (file.exists() && file.isFile()) {  // 파일이 존재하고 파일인지 확인
            boolean isDeleted = file.delete();  // 파일 삭제
            if (isDeleted) {
                log.info("File successfully deleted: {}", file.getAbsolutePath());
            } else {
                log.error("Failed to delete file: {}", file.getAbsolutePath());
            }
            return isDeleted;  // 삭제 성공 여부 반환
        } else {
            log.error("File does not exist or is not a file: {}", file.getAbsolutePath());
            return false;  // 파일이 존재하지 않거나 파일이 아니면 false 반환
        }
    }

    public boolean moveFolder(String oldFolderPath, String newFolderPath) {
        Path sourcePath = Paths.get(uploadPath,oldFolderPath);
        Path targetPath = Paths.get(uploadPath,newFolderPath);

        try {
            // 대상 폴더가 없으면 생성
            Files.createDirectories(targetPath);

            // 깊이 우선 탐색으로 디렉터리와 파일 모두 처리
            Files.walk(sourcePath)
                    .sorted(Comparator.reverseOrder()) // 하위 폴더부터 먼저 처리 (DFS)
                    .forEach(source -> {
                        try {
                            Path destination = targetPath.resolve(sourcePath.relativize(source));

                            if (Files.isDirectory(source)) {
                                // 디렉터리 생성
                                Files.createDirectories(destination);
                            } else {
                                // 파일 이동 (이미 존재하면 덮어쓰기)
                                Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
                            }
                        } catch (IOException e) {
                            System.err.println("이동 실패: " + source + " -> " + e.getMessage());
                        }
                    });

            // 원본 폴더 삭제
           // Files.deleteIfExists(sourcePath);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
