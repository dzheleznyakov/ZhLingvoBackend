import * as APPS from '../static/constants/apps';

export const getApp = (path) => {
    if (path.startsWith('/tutor'))
        return APPS.TUTOR;
    return APPS.DICTIONARY;
};
