package com.green.jobdone.admin.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.jobdone.admin.model.AdminDetailTypeInfoDto;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.util.List;

public class JsonTypeHandler extends BaseTypeHandler<List<AdminDetailTypeInfoDto>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(java.sql.PreparedStatement ps, int i, List<AdminDetailTypeInfoDto> parameter, JdbcType jdbcType) throws java.sql.SQLException {
        try {
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (IOException e) {
            throw new java.sql.SQLException("Error serializing JSON", e);
        }
    }

    @Override
    public List<AdminDetailTypeInfoDto> getNullableResult(java.sql.ResultSet rs, String columnName) throws java.sql.SQLException {
        try {
            String json = rs.getString(columnName);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<List<AdminDetailTypeInfoDto>>() {});
            }
        } catch (IOException e) {
            throw new java.sql.SQLException("Error deserializing JSON", e);
        }
        return null;
    }

    @Override
    public List<AdminDetailTypeInfoDto> getNullableResult(java.sql.ResultSet rs, int columnIndex) throws java.sql.SQLException {
        try {
            String json = rs.getString(columnIndex);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<List<AdminDetailTypeInfoDto>>() {});
            }
        } catch (IOException e) {
            throw new java.sql.SQLException("Error deserializing JSON", e);
        }
        return null;
    }

    @Override
    public List<AdminDetailTypeInfoDto> getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws java.sql.SQLException {
        try {
            String json = cs.getString(columnIndex);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<List<AdminDetailTypeInfoDto>>() {});
            }
        } catch (IOException e) {
            throw new java.sql.SQLException("Error deserializing JSON", e);
        }
        return null;
    }
}
