package com.github.rzah94.managerapp.controller;


import com.github.rzah94.managerapp.entity.Product;
import com.github.rzah94.managerapp.payload.UpdatedProductPayload;
import com.github.rzah94.managerapp.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/catalogue/products/{productId:\\d+}")
public class ProductController {
    private final ProductService productService;

    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") Long productId) {
        return this.productService.findProduct(productId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public String getProduct() {
        return "catalogue/products/product";
    }

    @GetMapping("/edit")
    public String getProductEditPage() {
        return "catalogue/products/edit";
    }

    @PostMapping("/edit")
    public String updateProduct(@ModelAttribute("product") Product product, UpdatedProductPayload payload) {
        this.productService.updateProduct(product.getId(), payload.title(), payload.details());
        return "redirect:/catalogue/products/%d".formatted(product.getId());
    }

    @PostMapping("/delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {

        this.productService.deleteProduct(product.getId());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response, Locale locale) {
        model.addAttribute("error",
                this.messageSource.getMessage(exception.getMessage(), new Object[0],
                        exception.getMessage(), locale));
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return "errors/404";
    }

}
