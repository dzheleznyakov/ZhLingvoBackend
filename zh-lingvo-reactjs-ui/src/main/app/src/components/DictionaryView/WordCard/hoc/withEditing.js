import React from 'react';
import { useSelector } from 'react-redux';

import { isEditingSelector } from '../../../../store/selectors';

export default (RegularComponent, EditingComponent) => props => {
    const isEditing = useSelector(isEditingSelector);
    return isEditing ? <EditingComponent {...props} /> : <RegularComponent {...props} />;
};
