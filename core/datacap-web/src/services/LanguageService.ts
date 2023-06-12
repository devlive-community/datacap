import FunctionsService from "@/services/settings/functions/FunctionsService";

export class LanguageService
{
  transSuggestions(items: string | any[], language: string)
  {
    const languageSugs = [];
    FunctionsService.getByPlugin(language)
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
              documentation: item.example,
              icon: items.includes(item.value),
            };
          });
        return newSug;
      });
  }
}
