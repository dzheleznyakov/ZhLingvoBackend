export const setCookie = (key: string, value: string, path?: string): void => {
  const pathSection = path ? `; path=${path}` : '; path=/';
  document.cookie = `${key}=${value}${pathSection}`;
};

export const getCookie = (key: string): string | null => {
  const tokenArray = document.cookie
    .split(';')
    .map((s) => s.trim())
    .filter((s) => s.startsWith(`${key}=`))
    .map((entry) => entry.split('=')[1]);
  return tokenArray.length ? tokenArray[0] : null;
};

export const deleteCookie = (key: string): void => {
  document.cookie = `${key}=; expires=Thu, 01 Jan 1970 00:00:00 UTC;`;
};
