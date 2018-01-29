/**
 * Issued when there is a collision of forced assignments
 * due to overlapping assignments or over allocation
 * M1 -> T4, M7 -> T4
 * M3 -> T2, M3 -> T9
 */
@SuppressWarnings("serial")
public class AssignmentException extends Exception {}
