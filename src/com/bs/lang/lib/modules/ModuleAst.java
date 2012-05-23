package com.bs.lang.lib.modules;

import com.bs.lang.BsObject;
import com.bs.lang.builtin.BsModule;
import com.bs.lang.lib.Loadable;

public class ModuleAst implements Loadable {

	@Override
	public String getName() {
		return "ast";
	}

	@Override
	public BsObject getModule() {
		BsObject ast = BsModule.create("ast");
		ast.setSlot(ModuleConst.AST);

		return ast;
	}

}
