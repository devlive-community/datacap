import {createI18n} from 'vue-i18n'
import messages from '@/i18n/langs'

const language = (navigator.language || 'en').toLocaleLowerCase()

const i18n = createI18n({
  fallbackLocale: 'en',
  globalInjection: true,
  legacy: false,
  // [intlify] Fall back to translate 'hello' key with 'en' locale.
  silentFallbackWarn: true,
  fallbackWarn: false,
  // [intlify] Not found 'hello' key in 'en' locale messages.
  silentTranslationWarn: true,
  missingWarn: false,
  locale: localStorage.getItem('lang') || language.split('-')[0] || 'en',
  messages
})

export default i18n
