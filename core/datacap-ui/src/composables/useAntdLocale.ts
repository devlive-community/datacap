import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import enUS from 'ant-design-vue/es/locale/en_US'
import type { Locale } from 'ant-design-vue/es/locale'

/**
 * 把应用当前 vue-i18n 语言映射到 ant-design-vue 的内置 locale，
 * 传给 <a-config-provider :locale>，让 antd 组件（分页、表格空数据、日期等）文案跟随全局语言。
 *
 * 这样迁移后不再需要 view-shadcn-ui 的 setLocale——antd 内置文案与业务 i18n 收敛为同一套来源。
 */
const LOCALE_MAP: Record<string, Locale> = {
    zh_cn: zhCN,
    'zh-cn': zhCN,
    zh: zhCN,
    en: enUS,
    en_us: enUS,
    'en-us': enUS
}

export function useAntdLocale() {
    const { locale } = useI18n()

    const antdLocale = computed<Locale>(() => {
        const key = (locale.value || '').toLowerCase()
        return LOCALE_MAP[key] || enUS
    })

    return { antdLocale }
}
