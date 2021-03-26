//        Job job = new JobBuilder<Document, Document>()
//            .batchSize(MAX_BATCH_SIZE)
//            .reader(new FileNetReader<>(actionExecutor, searchFolderQuery))
//            .writer(new FileNetOutputRecordWriter<>(actionExecutor))
//            .build();
//
//        JobExecutor jobExecutor = new JobExecutor();
//        JobReport report = jobExecutor.execute(job);
//        jobExecutor.shutdown();

    // Print the job execution report
    //  log.debug(report.toString());
    // Create queues
//        BlockingQueue<Record<Document>> workQueue1 = new LinkedBlockingQueue<>();
//        BlockingQueue<Record<Document>> workQueue2 = new LinkedBlockingQueue<>();
//        BlockingQueue<Record<Document>> joinQueue = new LinkedBlockingQueue<>();
//
//        // Build jobs
//        Job forkJob = buildForkJob("fork-job", searchFolderQuery, actionExecutor, asList(workQueue1, workQueue2));
//        Job workerJob1 = buildWorkerJob("worker-job1", workQueue1, joinQueue);
//        Job workerJob2 = buildWorkerJob("worker-job2", workQueue2, joinQueue);
//        Job joinJob = buildJoinJob("join-job", actionExecutor, joinQueue);
//
//        // Create a job executor to call jobs in parallel
//        JobExecutor jobExecutor = new JobExecutor(THREAD_POOL_SIZE);
//
//        // Submit jobs to run in parallel
//        jobExecutor.submitAll(forkJob, workerJob1, workerJob2, joinJob);
//
//        // Shutdown job executor
//        jobExecutor.shutdown();


//    private Job buildForkJob(String jobName, String searchFolderQuery, ActionExecutor actionExecutor,
//                             List<BlockingQueue<Record<Document>>> workQueues) {
//        return new JobBuilder<Document, Document>()
//            .named(jobName)
//            .reader(new FileNetReader<>(actionExecutor, searchFolderQuery))
//            .writer(new RoundRobinBlockingQueueRecordWriter<>(workQueues))
//            .build();
//    }
//
//    private Job buildWorkerJob(String jobName,
//                               BlockingQueue<Record<Document>> workQueue,
//                               BlockingQueue<Record<Document>> joinQueue) {
//        return new JobBuilder<Document, Document>()
//            .named(jobName)
//            .reader(new BlockingQueueRecordReader<>(workQueue, QUEUE_TIMEOUT))
//            .writer(new BlockingQueueRecordWriter<>(joinQueue))
//            .build();
//    }
//
//    private Job buildJoinJob(String jobName, ActionExecutor actionExecutor, BlockingQueue<Record<Document>> joinQueue) {
//        return new JobBuilder<Document, Document>()
//            .named(jobName)
//            .reader(new BlockingQueueRecordReader<>(joinQueue, QUEUE_TIMEOUT))
//            .writer(new FileNetOutputRecordWriter<>(actionExecutor))
//            .build();
//    }
