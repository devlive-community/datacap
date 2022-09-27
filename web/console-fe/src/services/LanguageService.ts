export class LanguageService {
  transSuggestions(items: string | any[]) {
    const newSug = [...items, 'and', 'or', '(', ')', 'select']
      .map((item) => {
        return {
          label: item,
          detail: !items.includes(item) ? 'symbol' : 'field',
          insertText: item,
          icon: items.includes(item),
        };
      });
    return newSug;
  }
}
