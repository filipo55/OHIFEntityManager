package com.mycompany.myapp.domain;

import org.springframework.data.mongodb.core.mapping.Document;


@Document
public enum MeasurementType
{
    LESION("LESION"), CANCER("CANCER"), UNKNOWN("UNKNOWN");
    private final String value;

    MeasurementType(String value)
    {
        this.value = value;
    }
    public static MeasurementType fromValue(String value)
    {
        if (value != null) {
            for (MeasurementType measurementType : values()) {
                if (measurementType.value.equals(value)) {
                    return measurementType;
                }
            }
        }
        throw new IllegalArgumentException("Invalid type: " + value);
    }
    public String toValue() {
        return value;
    }

    public static MeasurementType getDefault() {
        return UNKNOWN;
    }

}

