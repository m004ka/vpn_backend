package org.application.prod.yooKassa.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Item {
    private final String description = "Услуга настройки интернета";
    private final int quantity = 1;
    private final Amount amount;
    private final int vat_code = 1;
}
