import React from 'react';
import PropTypes from 'prop-types';

import OnHoverEditable from '../Editable/OnHoverEditable';
import NewPartButton from '../NewPartButton';
import Translation from './Translation';
import { TRANSLATION_EDIT, TRANSLATION_DELETE, TRANSLATION_NEW } from '../../../../../../static/constants/wordEditModalTypes';
import { translationType } from '../../wordTypes';

export const NULL_TRANSLATION = { id: -1, value: '' };

const EditableTranslation = props => {
    const { entry: translation, path, postfix } = props;

    if (translation === NULL_TRANSLATION)
        return <NewPartButton
            label="translation"
            modalType={TRANSLATION_NEW}
            path={path}
        />;

    return (
        <OnHoverEditable
            editModalType={TRANSLATION_EDIT}
            deleteModalType={TRANSLATION_DELETE}
            path={path}
        >
            <Translation entry={translation} postfix={postfix} />
        </OnHoverEditable>
    );
};

EditableTranslation.propTypes = {
    entry: translationType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
    postfix: PropTypes.string,
};

export default EditableTranslation;
