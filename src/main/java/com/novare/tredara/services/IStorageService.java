package com.novare.tredara.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.stream.Stream;


public interface IStorageService {

    void init();

    String store(MultipartFile file);

    String storeBase64(byte[] file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    void delete(String filename);
}
