//Automatically support lombok builder and Jackson       
ObjectMapper bean = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
                    @Override
                    public JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac) {
                        if (ac.hasAnnotation(JsonPOJOBuilder.class)) {//If no annotation present use default as empty prefix
                            return super.findPOJOBuilderConfig(ac);
                        }
                        return new JsonPOJOBuilder.Value("build", "");
                    }
                });
