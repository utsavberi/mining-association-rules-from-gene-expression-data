package parser;

import ruleMiner.AssociationRule;


interface TemplateRunner {
public boolean ruleMatchesTemplate(String inputQuery,AssociationRule associationRule);
}
