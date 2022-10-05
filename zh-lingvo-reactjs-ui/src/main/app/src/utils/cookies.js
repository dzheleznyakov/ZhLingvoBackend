export const setCookie = (key, value, path) => {
    const pathSection = path ? `; path=${path}` : '; path=/'
    document.cookie = `${key}=${value}${pathSection}`;
};

export const getCookie = key => {
    const tokenArray = document.cookie.split(';')
        .map(s => s.trim())
        .filter(s => s.startsWith(`${key}=`))
        .map(entry => entry.split('=')[1]);
    return tokenArray.length ? tokenArray[0] : null;
};

export const deleteCookie = key => {
    document.cookie = `${key}=; expires=Thu, 01 Jan 1970 00:00:00 UTC;`
};
