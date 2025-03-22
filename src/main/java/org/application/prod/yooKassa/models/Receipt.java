package org.application.prod.yooKassa.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Receipt {
    private Customer customer;
    private List<Item> items;
}
