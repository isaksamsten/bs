package com.bs.interpreter;

import com.bs.parser.tree.ArgumentsNode;
import com.bs.parser.tree.AssignNode;
import com.bs.parser.tree.BlockNode;
import com.bs.parser.tree.CallNode;
import com.bs.parser.tree.ExpressionNode;
import com.bs.parser.tree.ExpressionsNode;
import com.bs.parser.tree.MessageNode;
import com.bs.parser.tree.MessagesNode;
import com.bs.parser.tree.NumberNode;
import com.bs.parser.tree.StatementsNode;
import com.bs.parser.tree.StringNode;
import com.bs.parser.tree.IdentifierNode;

public interface Interpreter {

	Object visitNumber(NumberNode numberNode);

	Object visitVariable(IdentifierNode variableNode);

	Object visitString(StringNode stringNode);

	Object visitExpression(ExpressionNode exprNode);

	Object visitCall(CallNode callNode);

	Object visitExpressions(ExpressionsNode expressionListNode);

	Object visitMessage(MessageNode messageNode);

	Object visitMessages(MessagesNode messagesNode);

	Object visitStatements(StatementsNode statementsNode);

	Object visitAssign(AssignNode assignNode);

	Object visitBlock(BlockNode blockNode);

	Object visitArguments(ArgumentsNode argumentsNode);

}
