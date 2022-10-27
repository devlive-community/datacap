import filterLanguage from "@/services/language/index";

export class LanguageService
{
  transSuggestions(items: string | any[], language: string)
  {
    const languageSugs = [];
    const languageSupport = filterLanguage(language);
    if (languageSupport) {
      languageSugs.push(...languageSupport.keywords.keywords.map((item) => {
          return {value: item, detail: 'keyword'};
        }),
        ...languageSupport.operators.operators.map((item) => {
          return {value: item, detail: 'operator'};
        }),
        ...languageSupport.builtinFunctions.builtinFunctions.map((item) => {
          return {value: item, detail: 'function'};
        }));
    }
    const newSug = [...items, ...languageSugs]
      .map((item) => {
        return {
          label: item.value,
          detail: item.detail,
          insertText: item.value,
          icon: items.includes(item.value),
        };
      });
    return newSug;
  }
}
