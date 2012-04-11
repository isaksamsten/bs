 num(num(X)) --> [X], {number(X)}.
var(var(X)) --> [X], {atom(X),
		      X \== '(',
		      X \== ')',
		      X \== ']',
		      X \== '[',
		      X \== '{',
		      X \== '}',
		      X \== ','}.
str(str(X)) --> ['"'], atom(X), ['"'].

/*
 * Arguments are a list of names
 */ 
args(args(X)) --> var(X).
args(args(X, Y)) --> var(X), args(Y).


/*
 * A literal is a string, number or a variable.
 */
literal(X) --> var(X); num(X); str(X).
literal(X) --> ['('], expr(X), [')'].

/*
 * An expression can be a literal
 */
expr(expr(X)) --> literal(X).

/*
 * Calling a message (or list of messages) on a literal
 * (an what is returned)
 *
 * Eg. Object clone() clone()
 */
expr(call(X, Y)) --> literal(X), message_list(Y).


/*
 * A block is an argument list followed by a pipe and statement list
 *
 * Eg. [ | x, y | x show(y) ]
 */
expr(block(X, Y)) --> ['['], args(X), ['|'], statements(Y), [']'].
expr(block(X)) -->  ['['], ['|'], statements(X), [']'].
expr(list(X)) --> ['{'], expr_list(X), ['}'].


/*
 * A list of expressions, separated by commas
 *
 * Eg. 1, 2, Receiver message()
 */
expr_list(X) --> expr(X).
expr_list(exprs(X, Y)) --> expr(X), [','], expr_list(Y).

/*
 * A message is a literal followed by starting and ending parentheses,
 * with or without an expression list in between.
 *
 * Eg. message() or message(1,2,3), +(3), or + 3
 */
message(message(X)) --> var(X), ['('], [')'].
message(message(X, Y)) --> var(X), ['('], expr_list(Y), [')'].
message(message(X, Y)) --> var(X), expr_list(Y).

/*
 * A message list is a message follwed by a list of messages
 *
 * Eg. message() message2(2,2)
 */
message_list(X) --> message(X).
message_list(messages(X,Y)) --> message(X), message_list(Y).

/*
 * A statement is either an expression, or an assignment
 */
statement(assign(X, Y)) --> var(X), [:=], expr(Y), ['.'].
statement(X) --> expr(X), ['.'].

/*
 * A program is composed of a list of statements
 */  
program(X) --> statement(X).

statements(X) --> statement(X).
statements(statements(X, Y)) --> statement(X), statements(Y).

% var = Receiver message(arg, Receiver message()) message() message().
% 1 >=(2) isTrue([
%   Transcript show("hello world").
% ], [
%   Transcript show("Hello world %s", "isak").
% ]).
%
%  
%
% a = Object clone(10, 10) clone() clone()

