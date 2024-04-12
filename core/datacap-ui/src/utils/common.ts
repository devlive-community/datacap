const token = 'DataCapAuthToken'
const menu = 'DataCapAvailableMenus'
const userEditorConfigure = 'DataCapUserEditorConfigure'

/**
 * Retrieves the current user's ID from local storage.
 *
 * @return {number} The current user's ID.
 */
const getCurrentUserId = (): number => {
    return JSON.parse(localStorage.getItem(token) || '{}').id
}

/**
 * Retrieves the text based on the given origin value.
 *
 * @param {any} i18n - the internationalization object
 * @param {string} origin - the origin value to determine the text to retrieve
 * @return {string} the text based on the origin value
 */
const getText = (i18n: any, origin: string): string => {
    switch (origin) {
        case 'CREATED':
            return i18n.t('state.common.create')
        case 'RUNNING':
            return i18n.t('state.common.running')
        case 'SUCCESS':
            return i18n.t('state.common.success')
        case 'FAILURE':
            return i18n.t('state.common.failure')
        case 'STOPPED':
            return i18n.t('state.common.stop')
        case 'TIMEOUT':
            return i18n.t('state.common.timeout')
        case 'QUEUE':
            return i18n.t('state.common.queue')
        default:
            return origin
    }
}

/**
 * Returns the color based on the origin.
 *
 * @param {string} origin - The origin value.
 * @return {string} The color based on the origin.
 */
const getColor = (origin: string): string => {
    switch (origin) {
        case 'CREATED':
            return 'hsl(220.9 39.3% 11%)'
        case 'RUNNING':
            return 'hsl(221.2 83.2% 53.3%)'
        case 'SUCCESS':
            return 'hsl(142.1 76.2% 36.3%)'
        case 'FAILURE':
            return 'hsl(346.8 77.2% 49.8%)'
        case 'STOPPED':
            return '#17233d'
        case 'TIMEOUT':
            return 'hsl(47.9 95.8% 53.1%)'
        default:
            return 'hsl(24.6 95% 53.1%)'
    }
}

const fileToBase64 = (file: File): Promise<string> => {
    return new Promise((resolve, reject) => {
        const reader = new FileReader()
        reader.onload = () => {
            const base64String = reader.result as string
            resolve(base64String)
        }
        reader.onerror = reject
        reader.readAsDataURL(file)
    })
}

export default {
    token: token,
    menu: menu,
    getCurrentUserId: getCurrentUserId,
    userEditorConfigure: userEditorConfigure,
    getText: getText,
    getColor: getColor,
    fileToBase64: fileToBase64
}
