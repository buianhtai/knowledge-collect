public class FileNetOutputRecordWriter<P> implements RecordWriter<P> {

    private static final String DEFAULT_DIR = "/tmp/";

    private String targetDir;

    private final ActionExecutor actionExecutor;

    public FileNetOutputRecordWriter(final ActionExecutor actionExecutor) {
        super();
        this.actionExecutor = actionExecutor;
        this.targetDir = DEFAULT_DIR;
    }

    public FileNetOutputRecordWriter(final ActionExecutor actionExecutor, final String targetDir) {
        this.actionExecutor = actionExecutor;
        this.targetDir = targetDir;
    }

    @Override
    @SneakyThrows
    public void writeRecords(Batch<P> batch) {
        for (Record<P> record : batch) {
            actionExecutor.execute(context -> {
                Document doc = (Document) record.getPayload();
                log.info("Document : " + doc.get_Id().toString());

                Properties props = doc.getProperties();
                Constants.GR_FORM_FETCHED_PROPERTIES.forEach((k) -> {
                        Object value = props.getObjectValue(k) != null ? props.getObjectValue(k) : "";
                        log.info("Key:  " + k + " - Value: " + value);
                    }
                );

                ContentElementList contentElementList = doc.get_ContentElements();
                int index = 0;
                if (contentElementList != null && contentElementList.size() > 0) {
                    ContentTransfer contentTransfer = (ContentTransfer) contentElementList.get(0);
                     InputStream inputStream = contentTransfer.accessContentStream();
                        String fileName = remove(doc.get_Id().toString()) + "_" + index + "." + normalize(contentTransfer.get_RetrievalName());
                        log.info("Copy file :  " + fileName);
                    Try.run(()-> FileUtils.copyToFile(inputStream, new File(targetDir + fileName)));
                }
                return null;
            });
        }
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }
}
