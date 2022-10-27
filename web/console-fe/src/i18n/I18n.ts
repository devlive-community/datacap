import {createI18n} from 'vue-i18n';
import messages from '@/i18n/langs/index';

const language = (navigator.language || 'en').toLocaleLowerCase();

const i18n = createI18n({
  fallbackLocale: 'zh_cn',
  globalInjection: true,
  legacy: false,
  locale: localStorage.getItem('lang') || language.split('-')[0] || 'en',
  messages
});

export default i18n
