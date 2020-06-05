package com.techstack.batch.job.write.sout;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SysOutItemWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> items) throws Exception {
        System.out.println("The size of this chunk was: " + items.size());

        items.forEach(item -> System.out.println(">> " + item));
    }
}
