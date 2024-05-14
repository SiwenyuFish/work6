package com.memo.transaction.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheMessage implements Serializable {
    private Long id;
    private Long key;
}
