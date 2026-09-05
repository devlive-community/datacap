import { computed, ref, watch } from 'vue'
import { theme } from 'ant-design-vue'

/**
 * 全局主题（明/暗）composable。
 *
 * - 单例状态：模块级 `isDark`，全应用共享，任意组件调用 useTheme() 都拿到同一个开关。
 * - 持久化：写入 localStorage，刷新后保持。
 * - 作用于两处：给 <html> 加 `dark` class（供自定义 CSS 用 `.dark xxx` 定制），
 *   同时导出 antd 的主题算法给 <a-config-provider :theme>。
 *
 * 迁移到 ant-design-vue 后，暗黑模式由 antd 的 darkAlgorithm 负责组件；
 * 自定义（CSS 手写）组件则通过 <html class="dark"> 上的类名做暗色适配。
 */
const STORAGE_KEY = 'datacap-theme'

const isDark = ref<boolean>(localStorage.getItem(STORAGE_KEY) === 'dark')

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

// 首次加载即应用一次
applyToDocument(isDark.value)

watch(isDark, (value) => {
    localStorage.setItem(STORAGE_KEY, value ? 'dark' : 'light')
    applyToDocument(value)
})

export function useTheme() {
    /** 传给 <a-config-provider :theme> 的主题配置。 */
    const antdTheme = computed(() => ({
        algorithm: isDark.value ? theme.darkAlgorithm : theme.defaultAlgorithm,
        token: {
            // 品牌主色，后续可按 DataCap 设计调整
            colorPrimary: '#1677ff',
            borderRadius: 6
        }
    }))

    const toggle = (): void => {
        isDark.value = !isDark.value
    }

    const setDark = (value: boolean): void => {
        isDark.value = value
    }

    return {
        isDark,
        antdTheme,
        toggle,
        setDark
    }
}
