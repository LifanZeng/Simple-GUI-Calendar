import java.time.DayOfWeek;

/**
 * Define an enum Enum_Day, use "M, T, W, R, F, A, S" to represent "DayOfWeek.MONDAY...DayOfWeek.SUNDAY."
 *
 */
public enum Enum_Day {
    M, T, W, R, F, A, S;

    /**
     * Let the name of enum map to a DayofWeek value.
     * @return DayofWeek.
     */
    public DayOfWeek get_DayofWeek(){
        switch (name()) {
            case "M":
                return DayOfWeek.MONDAY;
            case "T":
                return DayOfWeek.TUESDAY;
            case "W":
                return DayOfWeek.WEDNESDAY;
            case "R":
                return DayOfWeek.THURSDAY;
            case "F":
                return DayOfWeek.FRIDAY;
            case "A":
                return DayOfWeek.SATURDAY;
            case "S":
                return DayOfWeek.SUNDAY;
            default:
                return null;
        }
    }
}