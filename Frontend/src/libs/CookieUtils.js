class CookieUtils {
    static get(name) {
        let cookieName = `${encodeURIComponent(name)}=`,
        cookieStart = document.cookie.indexOf(cookieName),
        cookieValue = null

        if (cookieStart > -1) {
            let cookieEnd = document.cookie.indexOf(";", cookieStart)
            if (cookieEnd === -1) {
                cookieEnd = document.cookie.length
            }
            cookieValue = decodeURIComponent(document.cookie.substring(cookieStart+
            cookieName.length, cookieEnd))
        }
        return cookieValue
    }

    static set(name, value, options = {}) {
        let cookieText = `${encodeURIComponent(name)}=${encodeURIComponent(value)}`
        if (options.maxAge) {
            cookieText += `;max-age=${options.maxAge}`;
        }
        if (options.path) {
            cookieText += `;path=${options.path}`;
        }
        if (options.domain) {
            cookieText += `;domain=${options.domain}`;
        }
        if (options.secure) {
            cookieText += `;secure`;
        }
        if (options.sameSite) {
            cookieText += `;samesite=${options.sameSite}`;
        }
        document.cookie = cookieText
    }

    static unset(name) {
        CookieUtils.set(name, "", 0)
    }
}

export { CookieUtils }