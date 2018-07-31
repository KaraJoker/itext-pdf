package io.sign.pdf.controller;

import io.sign.pdf.signtrue.CreateSignature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "itext")
public class signController {

    final static CreateSignature createSignature = new CreateSignature();

    @RequestMapping(value = "sign")
    public String sign() {
        try {
            createSignature.sign50MNaive();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
