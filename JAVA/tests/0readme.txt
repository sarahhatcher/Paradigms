
1name_error: parser error name on two lines.
8diag: 9 penalties
8forbid: No valid sol
8forbid_noline: parser error (currently accepting inputs with no newline between things)
doublespace: parser error
end_line_spaces: some penalty. No error
extracol: parser error
extrarow: parser error (currently returning out of bounds error)
forcedforbid: No valid solution
forced_forbid_parser: parser error
forbid_wrong: parser error
forced_wrong: parser error
less_col: parser error
less_row: parser error
minimalisticexample: 8 penalty
missing_title: parser error ??? (we currently return a parser error, is that correct)
missing_too-near: parser error (does not handle missing things)
missing: parser error (does not handle missing things)
natural_numbers: large pen
negative_pen: error

  Suite of tests for order of error operations
orderspaces: parser error (happens before the invalid mach? this is the correct error?)
order_double_assign: partial assignment error happens before no valid solution
order_names: parser error (as too near task is before forbidden machine)

overwrite_near_pen_decreased: 11 penalty ( overwrite is correct)
overwrite_near_pen_increased: 12 penalty
sudoku: some penalty
titlespace: parser error
too_near_double_forced: no valid sol
too_near_input: parser error
too_wrong: parser error
zero: 0 penalty
zeroforbidden: 0 penalty
zeroforced: 0 penalty
zero_forced_forbidden: 0 penalty
