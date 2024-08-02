import java.util.concurrent.*;

public class CameraSystem2 {

    // Callback interfaces
    public interface SuccessCallback {
        void onCapture(Image image);
    }

    public interface FailureCallback {
        void onError(String message);
    }

    // Image class
    public static class Image {
        private final String data;

        public Image(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }

    // CaptureRequest class
    public static class CaptureRequest implements Comparable<CaptureRequest> {
        private final int urgency;
        private final SuccessCallback successCallback;
        private final FailureCallback failureCallback;

        public CaptureRequest(int urgency, SuccessCallback successCallback, FailureCallback failureCallback) {
            this.urgency = urgency;
            this.successCallback = successCallback;
            this.failureCallback = failureCallback;
        }

        public int getUrgency() {
            return urgency;
        }

        public void onSuccess(Image image) {
            successCallback.onCapture(image);
        }

        public void onFailure(String message) {
            failureCallback.onError(message);
        }

        @Override
        public int compareTo(CaptureRequest o) {
            return Integer.compare(o.getUrgency(), this.getUrgency());
        }
    }

    // PriorityBlockingQueue to handle requests based on urgency
    private final PriorityBlockingQueue<CaptureRequest> captureRequests = new PriorityBlockingQueue<>();

    // ExecutorService to manage threads
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    // Method to process requests
    public void processRequests() {
        // Simulate processing of requests
        while(true) {
            try {
                CaptureRequest request = captureRequests.take();
                executorService.submit(() -> {
                    try {
                        // Simulate camera capture
                        Thread.sleep(1000);
                        // Always simulate success
                        request.onSuccess(new Image("Captured image data"));
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to add a capture request
    public synchronized void addCaptureRequest(CaptureRequest request) {
        captureRequests.add(request);
    }

    public static void main(String[] args) {
        CameraSystem2 cameraSystem = new CameraSystem2();

        // Start processing requests in a separate thread
        new Thread(cameraSystem::processRequests).start();

        // Simulate separate clients by adding requests with different urgencies
        // Client 1
        cameraSystem.addCaptureRequest(new CaptureRequest(1,
                image -> System.out.println("Client 1 success callback: " + image.getData()),
                error -> System.out.println("Client 1 failure callback: " + error)
        ));

        // Client 2
        cameraSystem.addCaptureRequest(new CaptureRequest(2,
                image -> System.out.println("Client 2 success callback: " + image.getData()),
                error -> System.out.println("Client 2 failure callback: " + error)
        ));

        // Client 3
        cameraSystem.addCaptureRequest(new CaptureRequest(3,
                image -> System.out.println("Client 3 success callback: " + image.getData()),
                error -> System.out.println("Client 3 failure callback: " + error)
        ));

        // Client 4
        cameraSystem.addCaptureRequest(new CaptureRequest(4,
                image -> System.out.println("Client 4 success callback: " + image.getData()),
                error -> System.out.println("Client 4 failure callback: " + error)
        ));

        // Client 5
        cameraSystem.addCaptureRequest(new CaptureRequest(5,
                image -> System.out.println("Client 5 success callback: " + image.getData()),
                error -> System.out.println("Client 5 failure callback: " + error)
        ));
    }
}
