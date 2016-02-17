package com.peltashield.inferno.utils;

/**
 * Created by javier on 7/6/15.
 */
public class ArrayCommons {

    /**
     * <p>Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (substracts one from
     * their indices).</p>
     * <p/>
     * <p>This method returns a new array with the same elements of the input
     * array except the element on the specified position. The component
     * type of the returned array is always the same as that of the input
     * array.</p>
     * <p/>
     * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.</p>
     *
     * @param array the array to remove the element from, may not be <code>null</code>
     * @param index the position of the element to be removed
     * @return A new array containing the existing elements except the element
     * at the specified position.
     * @throws IndexOutOfBoundsException if the index is out of range
     *      (index < 0 || index >= array.length), or if the array is <code>null</code>.
     */
    public static int[] remove(int[] array, int index) {
        int length = array.length - 1;
        if (index < 0 || index > length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length + 1);
        }

        int[] result = new int[length];
        System.arraycopy(array, 0, result, 0, index);
        if (index < length) {
            System.arraycopy(array, index + 1, result, index, length - index);
        }

        return result;
    }

}
