public class test {
    public static void main(String[] args) {
        
    }

    int search(int[] array, int low, int high, int target) {
        int mid = 0;
        while(low <= high) {
            mid = (high - low) /2;
            if( array[mid] > target) {
                high = mid - 1;
            } else if (array[mid] < target) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}
