package com.bs.interpreter;

import com.bs.parser.tree.ArgumentsNode;
import com.bs.parser.tree.AssignNode;
import com.bs.parser.tree.BlockNode;
import com.bs.parser.tree.CallNode;
import com.bs.parser.tree.CharacterNode;
import com.bs.parser.tree.ExpressionNode;
import com.bs.parser.tree.ExpressionsNode;
import com.bs.parser.tree.IdentifierNode;
import com.bs.parser.tree.ListNode;
import com.bs.parser.tree.MessageNode;
import com.bs.parser.tree.MessagesNode;
import com.bs.parser.tree.Node;
import com.bs.parser.tree.NumberNode;
import com.bs.parser.tree.StatementsNode;
import com.bs.parser.tree.StringNode;
import com.bs.parser.tree.SymbolNode;

public interface Interpreter {

	Object interpret(Node node);

	Object interpretNumber(NumberNode numberNode);

	Object interpretVariable(IdentifierNode variableNode);

	Object interpretString(StringNode stringNode);

	Object interpretExpression(ExpressionNode exprNode);

	Object interpretCall(CallNode callNode);

	Object interpretExpressions(ExpressionsNode expressionListNode);

	Object interpretMessage(MessageNode messageNode);

	Object interpretMessages(MessagesNode messagesNode);

	Object interpretStatements(StatementsNode statementsNode);

	Object interpretAssign(AssignNode assignNode);

	Object interpretBlock(BlockNode blockNode);

	Object interpretArguments(ArgumentsNode argumentsNode);

	Object interpretList(ListNode listNode);

	Object interpretSymbol(SymbolNode symbolNode);

	Object interpretCharacter(CharacterNode characterNode);

}
