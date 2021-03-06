package com.amt.dflipflop.Controllers;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amt.dflipflop.Constants;
import com.amt.dflipflop.Entities.Category;
import com.amt.dflipflop.Entities.Product;
import com.amt.dflipflop.Services.CategoryService;
import com.amt.dflipflop.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.nio.file.*;
import java.util.Map;

import static com.amt.dflipflop.Constants.IS_PROD;

@Controller
public class StoreController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @GetMapping("/store")
    public String getStorePage(@RequestParam(value = "cat", required = false) Integer catId, Model model) {

        ArrayList<Product> products;
        ArrayList<Category> categories = categoryService.getNonEmpty();

        if(catId != null){
            products = productService.getProductsByCategory(catId);
            model.addAttribute("selected", catId);
        }
        else {
            products = productService.getAll();
        }


        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "store";
    }

    @GetMapping("/store/product/{id}")
    public String getStoreProduct(@PathVariable("id") Integer productId, Model model) {

        Product product = productService.get(productId);

        if (product == null) {
            return "redirect:/store/product";
        }

        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/store/add-product")
    public String getAddProductPage(Model model, HttpServletRequest request) {
        // Info user after product insertion
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        String status;
        if (inputFlashMap != null) {
            status = (String) inputFlashMap.get("message");
            model.addAttribute("status", status);
        }

        ArrayList<Category> categories = categoryService.getAll();
        model.addAttribute("product", new Product());
        model.addAttribute("listCategories", categories);
        return "add-product";
    }

    /**
     *
     * @param product The product get from the form (front-end)
     * @param multipartFile The stream for the picture
     * @param result State of the request
     * @param redirectAttrs Attributes attached when redirect
     * @return The redirection to a page
     * @throws IOException If write fail
     */
    @PostMapping(path="/store/add-product") // Map ONLY POST Requests
    public String addNewProduct (@ModelAttribute("product") Product product, @RequestParam("image") MultipartFile multipartFile, BindingResult result, RedirectAttributes redirectAttrs, Model model) throws IOException {
        final String uploadDir;
        if (IS_PROD){
            uploadDir = "/opt/tomcat/webapps/img"; //Prod
        } else {
            uploadDir = "src/main/resources/static/images"; //Dev
        }

        final String defaultImgName = "default.png";
        String fileName;

        // Error in the format of the data submitted
        if(result.hasErrors()){
            redirectAttrs.addFlashAttribute("message", "Something went wrong, please retry");
            return "add-product";
        }

        // Check for duplicate name
        Product duplicateProdDescription = productService.nameExist(product.getName());
        if (duplicateProdDescription != null){
            ArrayList<Category> categories = categoryService.getAll();
            model.addAttribute("listCategories", categories);
            model.addAttribute("status", "A product with the name \"" + duplicateProdDescription.getName() + "\" already exist");
            return "add-product";
        }

        // Process if an image has been selected
        if (!multipartFile.isEmpty()) {
            //Get name of the img uploaded
            fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            //Import name of image
            product.setImageName(fileName);

            //Upload and write img
            if(IS_PROD){
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(multipartFile.getSize());
                amazonS3Client.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
            }
            else{
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    Path filePath = Paths.get(uploadDir).resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioe) {
                    throw new IOException("Could not save image file: " + fileName, ioe);
                }
            }

        } else {
            product.setImageName(defaultImgName);
        }

        // Add the product via a product service
        productService.insert(product);

        redirectAttrs.addFlashAttribute("message", "Success");

        return "redirect:/store/add-product";
    }

    /**
     *
     * @param productId Id of product get from url
     * @param model Representation of the elements for the view
     * @param request Data from redirections
     * @return The page manage-product
     */
    @GetMapping("/store/manage-product/{id}")
    public String getGestProduct(@PathVariable("id") Integer productId, Model model, HttpServletRequest request) {
        // Info user after product modification
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        String status;
        if (inputFlashMap != null) {
            status = (String) inputFlashMap.get("message");
            model.addAttribute("status", status);
        }

        Product product = productService.get(productId);
        if (product == null) {
            return "redirect:/store/product";
        }

        ArrayList<Category> categories = categoryService.getAll();
        model.addAttribute("product", product);
        model.addAttribute("listCategories", categories);

        return "manage-product";
    }

    @PostMapping(path="/store/manage-product/{id}") // Map ONLY POST Requests
    public String manageProduct (@ModelAttribute("product") Product product, @RequestParam("image") MultipartFile multipartFile, BindingResult result, RedirectAttributes redirectAttrs, Model model) throws IOException {
        final String uploadDir;
        if (IS_PROD){
            uploadDir = "/opt/tomcat/webapps/img"; //Prod
        } else {
            uploadDir = "src/main/resources/static/images"; //Dev
        }

        String fileName;

        // Error in the format of the data submitted
        if(result.hasErrors()){
            redirectAttrs.addFlashAttribute("message", "Something went wrong, please retry");
            return "add-product";
        }

        // Check for duplicate name
        Product duplicateProdDescription = productService.nameExistAndDifferFromId(product.getName(), product.getId());
        if (duplicateProdDescription != null){
            ArrayList<Category> categories = categoryService.getAll();
            model.addAttribute("listCategories", categories);
            model.addAttribute("status", "A product with the name \"" + duplicateProdDescription.getName() + "\" already exist");
            return "manage-product";
        }

        // Process if an image has been selected
        if (!multipartFile.isEmpty()) {
            //Get name of the img uploaded
            fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            //Import name of image
            product.setImageName(fileName);

            //Upload and write img
            if(IS_PROD){
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(multipartFile.getSize());
                amazonS3Client.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
            }
            else{
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    Path filePath = Paths.get(uploadDir).resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioe) {
                    throw new IOException("Could not save image file: " + fileName, ioe);
                }
            }
        }

        // Update the product via a product service
        productService.update(product);

        redirectAttrs.addFlashAttribute("message", "Success");

        return "redirect:/store/manage-product/" + product.getId();
    }

}
