import java.util.Scanner;

public class PrimeDate {

    private static final String[] DATE_FORMATS = {"DD-MM-YYYY", "MM-DD-YYYY", "YYYY-MM-DD"};
    private static final int[] DAYS_IN_A_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static class Date {
        
        public int year;
        public int month;
        public int day;

        public Date(int first, int second, int third, int format) {

            this.day = first;
            this.month = second;
            this.year = third;

            if(format == 1) {
                this.day = second;
                this.month = first;
            }
            else if(format == 2) {
                this.day = third;
                this.year = first;
            }
        }

        public static int toNumber(int year, int month, int day, int format) {

            String first = String.format("%2d", day).replace(' ', '0');
            String second = String.format("%2d", month).replace(' ', '0');
            String third = Integer.toString(year);
    
            if(format == 1) {
                String temp = first;
                first = second;
                second = temp;
            }
            else if(format == 2) {
                String temp = first;
                first = third;
                third = temp;
            }
    
            return Integer.parseInt(first+second+third);
        }

        public static String toString(Date date, int format) {
            return Date.toString(date.year, date.month, date.day, format);
        }

        public static String toString(int year, int month, int day, int format) {

            String first = String.format("%2d", day).replace(' ', '0');
            String second = String.format("%2d", month).replace(' ', '0');
            String third = String.format("%4d", year).replace(' ', '0');
    
            if(format == 1) {
                String temp = first;
                first = second;
                second = temp;
            }
            else if(format == 2) {
                String temp = first;
                first = third;
                third = temp;
            }

            return String.format("%s-%s-%s", first, second, third);
        }
    }

    private static int removeFirstDigit(int num) {

        return num % (int) Math.pow(10, (int) Math.log10(num));
    }

    private static int countDigits(int num) {

        int digits = 0;

        while(num > 0) {
            num /= 10;
            digits++;
        }

        return digits;
    }

    private static Boolean isPrime(int num) {

        Boolean prime = true;

        if(num != 2 && num % 2 == 0) prime = false;

        else {
            for(int n=3; n < (int) Math.sqrt(num) && prime; n+=2) {
                if(num % n == 0) prime = false;
            }
        }

        return prime;
    }

    private static int getFormat(Scanner sc) {

        int formatNum = 0;
        Boolean correctInput = false;

        while(!correctInput) {

            System.out.print("Choose a date format (0)[DD-MM-YYYY], (1)[MM-DD-YYYY], (2)[YYYY-MM-DD]: ");

            String format = sc.nextLine();

            try {
                formatNum = Integer.parseInt(format);
            
                if(0 <= formatNum && formatNum < 3) correctInput = true;
                else System.out.println(format + " is not a possible format!");
            }
            catch (Exception e) {
                System.out.println(format + " is not a possible format!");
            }   
        }

        return formatNum;
    }

    private static Date getDate(Scanner sc, int df) {

        int first=0, second=0, third=0;
        Boolean correctInput = false;

        while(!correctInput) {

            System.out.print("Enter the starting date (" + DATE_FORMATS[df] + "): ");
            String inputDate = sc.nextLine();
            String[] dateData = inputDate.split("-");

            try {
                if(dateData.length == 3) {
                    
                    first = Integer.parseInt(dateData[0]);
                    second = Integer.parseInt(dateData[1]);
                    third = Integer.parseInt(dateData[2]);

                    correctInput = true;
                }
                else System.out.println(inputDate + " is not a possible date!");
            }
            catch (Exception e) {
                System.out.println(inputDate + " is not a possible date!");
            }
        }

        return new Date(first, second, third, df);
    }

    public static void main(String[] input) {

        Scanner scanner = new Scanner(System.in);

        int dateFormat = getFormat(scanner);

        Date startDate = getDate(scanner, dateFormat);
        Date endDate = getDate(scanner, dateFormat);

        System.out.print("Choose output type (0)[[Print and count all prime dates]], (1)[Only count all prime dates]: ");
        String outputType = scanner.nextLine();

        Boolean print = false;
        if(outputType.equals("0")) print = true;

        System.out.println();

        int dates = 0;
        int primeDates = 0;

        for(int year = startDate.year; year <= endDate.year; year++) {

            int leapYear = (year % 4 == 0) ? 1 : 0;

            int monthStart = (year == startDate.year) ? startDate.month : 1;
            int monthEnd = (year == endDate.year) ? endDate.month : 12;

            for(int month = monthStart; month <= monthEnd; month++) {

                int febuary = (month == 2) ? 1 : 0;

                int dayStart = (month == startDate.month && year == startDate.year) ? startDate.day : 1;
                int dayEnd = (month == endDate.month && year == endDate.year) ? endDate.day : 31;

                for(int day = dayStart; day <= dayEnd && day <= DAYS_IN_A_MONTH[month-1] + leapYear*febuary; day++) {

                    dates++;
                    	
                    int number = Date.toNumber(year, month, day, dateFormat);
                    Boolean prime = true;

                    for(int d = 0; d < countDigits(number) && prime; d++) {

                        prime = isPrime(number);
                        number = removeFirstDigit(number);
                    }

                    if(prime) {
                        primeDates++;
                        if(print) System.out.println(Date.toString(year, month, day, dateFormat));
                    }
                }
            }
        }

        System.out.printf("A total of %d dates between %s and %s were counted. Out of those %d were prime.\n", dates, Date.toString(startDate, dateFormat), Date.toString(endDate, dateFormat), primeDates);

        scanner.close();
    }
}