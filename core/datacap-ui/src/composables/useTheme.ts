import { computed, ref, watch } from 'vue'
import { theme } from 'ant-design-vue'

/**
 * 全局主题（明/暗 + 品牌色板）composable。
 *
 * - 单例状态：模块级 `isDark` / `brandKey`，全应用共享。
 * - 持久化：localStorage（datacap-theme / datacap-brand），刷新后保持。
 * - 作用于三处：
 *   1. <html> 上的 `dark` class 与 `data-theme` 属性（自定义 CSS 用）；
 *   2. <a-config-provider :theme> 的 antd 算法与品牌 token（antd 组件用）；
 *   3. src/styles/theme.css 里的 --dc-* CSS 变量（暗色通过 html.dark 覆盖，
 *      品牌变量如需多品牌在 styles/theme.css 中按 data-brand 覆盖）。
 *
 * 后续在用户偏好中支持主题设置：调用 setDark / setBrand 即可，
 * 无需改动任何组件。
 */
const DARK_KEY = 'datacap-theme'
const BRAND_KEY = 'datacap-brand'

/** 品牌色板注册表：后续新增主题在这里登记即可 */
export interface BrandTheme
{
    key: string
    label: string
    /** 传给 antd token 的品牌色 */
    antd: {
        colorPrimary: string
        colorLink: string
    }
}

const BRANDS: Record<string, BrandTheme> = {
    datacap: {
        key: 'datacap',
        label: 'DataCap Green',
        antd: {
            colorPrimary: '#0A8F67',
            colorLink: '#0A8F67'
        }
    }
}

const isDark = ref<boolean>(localStorage.getItem(DARK_KEY) === 'dark')
const brandKey = ref<string>(localStorage.getItem(BRAND_KEY) || 'datacap')

function applyToDocument(dark: boolean): void {
    const el = document.documentElement
    if (dark) {
        el.classList.add('dark')
    }
    else {
        el.classList.remove('dark')
    }
    el.setAttribute('data-theme', dark ? 'dark' : 'light')
}

function applyBrand(key: string): void {
    const el = document.documentElement
    if (BRANDS[key]) {
        el.setAttribute('data-brand', key)
    }
    else {
        el.removeAttribute('data-brand')
    }
}

// 首次加载即应用一次
applyToDocument(isDark.value)
applyBrand(brandKey.value)

watch(isDark, (value) => {
    localStorage.setItem(DARK_KEY, value ? 'dark' : 'light')
    applyToDocument(value)
})

watch(brandKey, (value) => {
    localStorage.setItem(BRAND_KEY, value)
    applyBrand(value)
})

export function useTheme() {
    const brand = computed<BrandTheme>(() => BRANDS[brandKey.value] || BRANDS.datacap)

    /** 传给 <a-config-provider :theme> 的主题配置。 */
    const antdTheme = computed(() => ({
        algorithm: isDark.value ? theme.darkAlgorithm : theme.defaultAlgorithm,
        token: {
            // 品牌色（design/ui-color-system.md）
            colorPrimary: brand.value.antd.colorPrimary,
            colorLink: brand.value.antd.colorLink,
            // 状态色对齐 ui-color-system.md 第 8 节
            colorSuccess: '#16A66A',
            colorWarning: '#E5A11A',
            colorError: '#DC5656',
            colorInfo: '#3B82F6',
            // 布局层透明，露出 body 的环境光背景
            colorBgLayout: 'transparent',
            borderRadius: 8
        }
    }))

    const toggle = (): void => {
        isDark.value = !isDark.value
    }

    const setDark = (value: boolean): void => {
        isDark.value = value
    }

    /** 切换品牌色板（后续用户偏好的主题设置入口） */
    const setBrand = (key: string): void => {
        if (BRANDS[key]) {
            brandKey.value = key
        }
    }

    return {
        isDark,
        brand,
        brands: BRANDS,
        antdTheme,
        toggle,
        setDark,
        setBrand
    }
}
