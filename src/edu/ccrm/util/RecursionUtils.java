package edu.ccrm.util;

import edu.ccrm.domain.*;
import java.util.*;

/**
 * SIMPLE Utility class demonstrating recursion in various scenarios
 * All methods are basic and error-free
 */
public class RecursionUtils {

    /**
     * Calculate factorial using recursion
     */
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial not defined for negative numbers");
        }
        if (n <= 1) {
            return 1; // Base case
        }
        return n * factorial(n - 1); // Recursive case
    }

    /**
     * Calculate Fibonacci number using recursion
     */
    public static long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Fibonacci not defined for negative numbers");
        }
        if (n <= 1) {
            return n; // Base cases: fib(0) = 0, fib(1) = 1
        }
        return fibonacci(n - 1) + fibonacci(n - 2); // Recursive case
    }

    /**
     * Binary search using recursion
     */
    public static int binarySearch(List<String> sortedList, String target, int left, int right) {
        if (left > right) {
            return -1; // Base case: not found
        }

        int mid = left + (right - left) / 2;
        String midValue = sortedList.get(mid);

        int comparison = midValue.compareTo(target);

        if (comparison == 0) {
            return mid; // Base case: found
        } else if (comparison > 0) {
            return binarySearch(sortedList, target, left, mid - 1); // Search left half
        } else {
            return binarySearch(sortedList, target, mid + 1, right); // Search right half
        }
    }

    /**
     * Binary search wrapper method
     */
    public static int binarySearch(List<String> sortedList, String target) {
        if (sortedList == null || sortedList.isEmpty()) {
            return -1;
        }
        return binarySearch(sortedList, target, 0, sortedList.size() - 1);
    }

    /**
     * Calculate sum of digits using recursion
     */
    public static int sumOfDigits(int number) {
        number = Math.abs(number); // Work with absolute value

        if (number < 10) {
            return number; // Base case: single digit
        }
        return (number % 10) + sumOfDigits(number / 10); // Recursive case
    }

    /**
     * Calculate power using recursion
     */
    public static long power(int base, int exponent) {
        if (exponent < 0) {
            throw new IllegalArgumentException("Negative exponents not supported");
        }
        if (exponent == 0) {
            return 1; // Base case: any number to power 0 is 1
        }
        if (exponent == 1) {
            return base; // Base case: any number to power 1 is itself
        }

        return base * power(base, exponent - 1); // Recursive case
    }

    /**
     * Count occurrences of a character in a string recursively
     */
    public static int countChar(String str, char ch, int index) {
        if (str == null || index >= str.length()) {
            return 0; // Base case: end of string
        }

        int count = (str.charAt(index) == ch) ? 1 : 0;
        return count + countChar(str, ch, index + 1); // Recursive case
    }

    /**
     * Count character occurrences wrapper method
     */
    public static int countChar(String str, char ch) {
        return countChar(str, ch, 0);
    }

    /**
     * Reverse a string using recursion
     */
    public static String reverseString(String str) {
        if (str == null || str.length() <= 1) {
            return str; // Base case: null, empty, or single character
        }

        return reverseString(str.substring(1)) + str.charAt(0); // Recursive case
    }

    /**
     * Calculate sum of array elements recursively
     */
    public static int arraySum(int[] arr, int index) {
        if (arr == null || index >= arr.length) {
            return 0; // Base case: end of array
        }

        return arr[index] + arraySum(arr, index + 1); // Recursive case
    }

    /**
     * Array sum wrapper method
     */
    public static int arraySum(int[] arr) {
        return arraySum(arr, 0);
    }

    /**
     * Find maximum element in array recursively
     */
    public static int findMax(int[] arr, int index, int currentMax) {
        if (arr == null || index >= arr.length) {
            return currentMax; // Base case: end of array
        }

        int newMax = Math.max(currentMax, arr[index]);
        return findMax(arr, index + 1, newMax); // Recursive case
    }

    /**
     * Find maximum wrapper method
     */
    public static int findMax(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        return findMax(arr, 1, arr[0]);
    }

    /**
     * Generate student IDs recursively
     */
    public static List<String> generateStudentIds(String prefix, int count, int current, List<String> result) {
        if (current > count) {
            return result; // Base case: generated all IDs
        }

        String id = prefix + String.format("%03d", current);
        result.add(id);

        return generateStudentIds(prefix, count, current + 1, result); // Recursive case
    }

    /**
     * Generate student IDs wrapper method
     */
    public static List<String> generateStudentIds(String prefix, int count) {
        if (count <= 0) {
            return new ArrayList<>();
        }
        return generateStudentIds(prefix, count, 1, new ArrayList<>());
    }

    /**
     * Check if string is palindrome recursively
     */
    public static boolean isPalindrome(String str, int left, int right) {
        if (str == null) {
            return false;
        }

        if (left >= right) {
            return true; // Base case: single character or empty string
        }

        if (str.charAt(left) != str.charAt(right)) {
            return false; // Base case: characters don't match
        }

        return isPalindrome(str, left + 1, right - 1); // Recursive case
    }

    /**
     * Palindrome check wrapper method
     */
    public static boolean isPalindrome(String str) {
        if (str == null || str.length() <= 1) {
            return true;
        }
        return isPalindrome(str.toLowerCase(), 0, str.length() - 1);
    }

    /**
     * Count students in department recursively (simple version)
     */
    public static int countStudentsInDepartment(List<Student> students, String department, int index) {
        if (students == null || index >= students.size()) {
            return 0; // Base case: no more students
        }

        Student student = students.get(index);
        int count = 0;

        if (department == null) {
            if (student.getDepartment() == null) {
                count = 1;
            }
        } else if (department.equals(student.getDepartment())) {
            count = 1;
        }

        return count + countStudentsInDepartment(students, department, index + 1);
    }

    /**
     * Count students wrapper method
     */
    public static int countStudentsInDepartment(List<Student> students, String department) {
        return countStudentsInDepartment(students, department, 0);
    }

    /**
     * Demonstration method showing recursion examples
     */
    public static void demonstrateRecursion() {
        System.out.println("\n🔄 Demonstrating Recursive Functions:");
        System.out.println("=".repeat(50));

        // Factorial demonstration
        System.out.println("📊 Factorial Examples:");
        for (int i = 0; i <= 5; i++) {
            System.out.println("  " + i + "! = " + factorial(i));
        }

        // Fibonacci demonstration
        System.out.println("\n📊 Fibonacci Examples:");
        for (int i = 0; i <= 8; i++) {
            System.out.println("  fib(" + i + ") = " + fibonacci(i));
        }

        // Sum of digits demonstration
        System.out.println("\n📊 Sum of Digits Examples:");
        int[] numbers = {123, 456, 789, 9999};
        for (int num : numbers) {
            System.out.println("  Sum of digits in " + num + " = " + sumOfDigits(num));
        }

        // Power calculation
        System.out.println("\n📊 Power Calculation Examples:");
        System.out.println("  2^3 = " + power(2, 3));
        System.out.println("  3^4 = " + power(3, 4));
        System.out.println("  5^2 = " + power(5, 2));

        // String operations
        System.out.println("\n📊 String Operations:");
        String testStr = "hello world";
        System.out.println("  Counting 'l' in '" + testStr + "': " + countChar(testStr, 'l'));
        System.out.println("  Reverse of '" + testStr + "': " + reverseString(testStr));
        System.out.println("  Is 'racecar' a palindrome? " + isPalindrome("racecar"));
        System.out.println("  Is 'hello' a palindrome? " + isPalindrome("hello"));

        // Array operations
        int[] testArray = {1, 2, 3, 4, 5};
        System.out.println("\n📊 Array Operations:");
        System.out.println("  Sum of [1,2,3,4,5]: " + arraySum(testArray));
        System.out.println("  Maximum in [1,2,3,4,5]: " + findMax(testArray));

        // Student ID generation
        System.out.println("\n📊 Generated Student IDs:");
        List<String> ids = generateStudentIds("S", 5);
        System.out.println("  " + ids);

        System.out.println("=".repeat(50));
    }

    /**
     * Demonstrate binary search
     */
    public static void demonstrateBinarySearch() {
        System.out.println("\n🔍 Binary Search Demonstration:");

        List<String> sortedList = Arrays.asList("apple", "banana", "cherry", "date", "elderberry");
        System.out.println("Sorted list: " + sortedList);

        String[] searchTerms = {"banana", "date", "grape", "apple"};

        for (String term : searchTerms) {
            int index = binarySearch(sortedList, term);
            if (index >= 0) {
                System.out.println("  '" + term + "' found at index: " + index);
            } else {
                System.out.println("  '" + term + "' not found");
            }
        }
    }
}