package com.elipso.mineserver.controller;

import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ImageController {

    private final AtomicLong counter = new AtomicLong();

    @ResponseBody
    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@RequestParam(value="id") String id) throws IOException {
        String resourceLocation = "images/" + id + ".jpg";
        File imageFile = ResourceUtils.getFile(resourceLocation);
        return Files.readAllBytes(imageFile.toPath());
    }

    @ResponseBody
    @RequestMapping(value = "/id", method = RequestMethod.GET)
    public String getId() {
        return Long.toString(counter.incrementAndGet());
    }

    @RequestMapping(value = "/addRating", method = RequestMethod.POST)
    public synchronized void addRating(@RequestParam(value="id") String id,
                                       @RequestParam(value="rating") String rating,
                                       @RequestParam(value="user") String user) {
        String entry = id + " " + rating + " " + user + "\n";
        try {
            File resultsFile = ResourceUtils.getFile("results.txt");
            Files.write(resultsFile.toPath(), entry.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/info")
    public String root() throws FileNotFoundException {
        File resultsFile = ResourceUtils.getFile("results.txt");
        Scanner input = new Scanner(resultsFile);
        String res = "";
        while (input.hasNextLine()) {
            res += input.nextLine() + "<br />\n";
        }
        return res;
    }
}
