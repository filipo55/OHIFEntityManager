package com.mycompany.myapp.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public enum MeasurementType
{
    LESION,
    CANCER
}
