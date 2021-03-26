public class FileNetReader<T> implements RecordReader<T> {
    private static final int DEFAULT_MAX_RESULT = 100;
    private static final String DATASOURCE_NAME ="fileNet";

    private ActionExecutor actionExecutor;

    private String query;

    private PageIterator pageIterator;

    private final int pageSize;

    Object[] records;

    Iterator iterator;

    private long currentRecordNumber;

    public FileNetReader(final ActionExecutor actionExecutor, final String query) {
        this.actionExecutor = actionExecutor;
        this.query = query;
        this.pageSize = DEFAULT_MAX_RESULT;
    }


    @Override
    public void open() throws Exception {
         actionExecutor.execute(context -> {

            SearchSQL searchSQL = new SearchSQL(query);
            SearchScope searchScope = new SearchScope(context.getDefaultObjectStore());

            IndependentObjectSet resultsObjectSet = searchScope.fetchObjects(searchSQL, pageSize, null, true);

            pageIterator = resultsObjectSet.pageIterator();
            if(pageIterator.nextPage()){
               // pageIterator.nextPage();
                records = pageIterator.getCurrentPage();
                iterator = Arrays.stream(records).iterator();
            }
            return null;
        });

    }

    private boolean hasNextRecord() {
        if (!iterator.hasNext() && pageIterator.nextPage()) {
            records = pageIterator.getCurrentPage();
            iterator = Arrays.stream(records).iterator();
        }
        return iterator.hasNext();
    }

    @Override
    public Record<T> readRecord() throws Exception {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), LocalDateTime.now());
        if (hasNextRecord()) {
            return new GenericRecord<>(header, (T) iterator.next());
        } else {
            return null;
        }
    }

    @Override
    public void close() throws Exception {

    }

    private String getDataSourceName() {
        return DATASOURCE_NAME;
    }
}
