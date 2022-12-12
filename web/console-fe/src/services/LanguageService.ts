import FunctionService from "@/services/settings/function/FunctionService";

export class LanguageService
{
  transSuggestions(items: string | any[], language: string)
  {
    const languageSugs = [];
    FunctionService.getByPlugin(language)
      .then((response) => {
        if (response.status) {
          response.data.content.forEach(value => {
            languageSugs.push(value);
          });
        }
      })
      .finally(() => {
        const newSug = [...items, ...languageSugs]
          .map((item) => {
            return {
              label: item.name,
              detail: item.detail,
              insertText: item.value,
              description: item.description,
              icon: items.includes(item.value),
            };
          });
        return newSug;
      });
  }
}
