package com.suncloth.suncloth.controller.file_test;

import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.File;
import com.suncloth.suncloth.repository.FileRepository;
import com.suncloth.suncloth.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class FileController {

    private final FileService fileService;

    private final FileRepository fileRepository;

    // upload
    @GetMapping("/upload")
    public String testUploadForm() {

        return "/file/upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file
            , @RequestParam("files") List<MultipartFile> files
            , @RequestParam("cloth") Cloth cloth) throws IOException {
        File mainFile = fileService.saveFile(file);
        mainFile.setFileType("main");
        mainFile.setCloth(cloth);
        fileRepository.save(mainFile);

        for (MultipartFile multipartFile : files) {
            File subFile = fileService.saveFile(multipartFile);
            subFile.setFileType("sub");
            subFile.setCloth(cloth);
            fileRepository.save(subFile);
        }

        return "/file/upload";
    }

    // Download
    @GetMapping("/view")
    public String view(Model model) {

        List<File> files = fileRepository.findAll();
        model.addAttribute("all",files);
        return "/file/view";
    }

    //   이미지 출력
    @GetMapping("/images/{fileId}")
    @ResponseBody
    public Resource downloadImage(@PathVariable("fileId") Long id, Model model) throws IOException{

        File file = fileRepository.findById(id).orElse(null);
        return new UrlResource("file:" + file.getSavedPath());
    }

    // 첨부 파일 다운로드
    @GetMapping("/attach/{id}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long id) throws MalformedURLException {

        File file = fileRepository.findById(id).orElse(null);

        UrlResource resource = new UrlResource("file:" + file.getSavedPath());

        String encodedFileName = UriUtils.encode(file.getOrgNm(), StandardCharsets.UTF_8);

        // 파일 다운로드 대화상자가 뜨도록 하는 헤더를 설정해주는 것
        // Content-Disposition 헤더에 attachment; filename="업로드 파일명" 값을 준다.
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition).body(resource);
    }

}
