package project.society.web.timeblocks.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Day index out of bounds.")
public class DayIndexOutOfBoundsException extends RuntimeException {}
