package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.ProductQueryDto;
import com.d0dd.wms.entity.ProdSku;
import com.d0dd.wms.service.ProdSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.List;

@RestController
@RequestMapping("/prod/sku")
public class ProdSkuController {

    @Autowired
    private ProdSkuService prodSkuService;

    @GetMapping("/list")
    public Result<List<ProdSku>> list(ProductQueryDto queryDto) {
        return Result.success(prodSkuService.list(queryDto));
    }

    @GetMapping("/{id}")
    public Result<ProdSku> getById(@PathVariable Long id) {
        return Result.success(prodSkuService.getById(id));
    }

    @GetMapping("/generate-code")
    public Result<String> generateCode() {
        return Result.success(prodSkuService.generateSkuCode());
    }

    @PostMapping
    public Result<String> save(@RequestBody ProdSku prodSku) {
        prodSkuService.save(prodSku);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody ProdSku prodSku) {
        prodSkuService.update(prodSku);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        prodSkuService.removeById(id);
        return Result.success("Deleted successfully");
    }

    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            return Result.error("仅支持上传图片文件");
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null) {
            int idx = originalFilename.lastIndexOf('.');
            if (idx >= 0 && idx < originalFilename.length() - 1) {
                suffix = originalFilename.substring(idx).toLowerCase(Locale.ROOT);
            }
        }

        Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads", "prod-sku");
        Files.createDirectories(uploadDir);

        String filename = System.currentTimeMillis() + "-" + java.util.UUID.randomUUID() + suffix;
        Path targetPath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return Result.success("/uploads/prod-sku/" + filename);
    }
}
