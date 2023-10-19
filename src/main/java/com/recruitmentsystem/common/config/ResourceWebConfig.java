//package com.recruitmentsystem.common.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class ResourceWebConfig implements WebMvcConfigurer {
//    //    final Environment environment;
////
////    public ResourceWebConfig(Environment environment) {
////        this.environment = environment;
////    }
////
//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
////        String location = environment.getProperty("app.file.storage.mapping");
////        registry.addResourceHandler("/upload/**").addResourceLocations(location);
//            registry
//                    .addResourceHandler("/upload/**")
//                    .addResourceLocations("file:src/main/resources/static/image/user_profile/");
//    }
////    @Override
////    public void addResourceHandlers(ResourceHandlerRegistry registry) {
////        exposeDirectory("src/main/resources/static/image/user_profile/", registry);
////    }
////
////    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
////        Path uploadDir = Paths.get(dirName);
////        System.out.println(uploadDir);
////        String uploadPath = uploadDir.toFile().getAbsolutePath();
////        System.out.println(uploadPath);
////        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
////        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + uploadPath + "/");
////    }
//}
