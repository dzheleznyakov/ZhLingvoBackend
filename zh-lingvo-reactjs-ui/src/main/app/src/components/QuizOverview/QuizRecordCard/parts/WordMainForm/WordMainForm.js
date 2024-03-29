import React from 'react';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './WordMainForm.module.scss';
import { MAIN_FORM } from '../../../../../static/constants/quizRecordEditModalTypes';
import { quizRecordIsEditingSelector } from '../../../../../store/selectors';
import OnHoverEditable from '../Editable/OnHoverEditable';

const WordMainForm = props => {
    const { children: mainForm } = props;
    const isEditing = useSelector(quizRecordIsEditingSelector);

    return (
        <OnHoverEditable
            isEditing={isEditing}
            editModalType={MAIN_FORM}
            path={null}
        >
            <div className={classes.WordMainForm}>
                {mainForm}
            </div>
        </OnHoverEditable>
    );
};

WordMainForm.propTypes = {
    children: PropTypes.string.isRequired,
};

export default WordMainForm;
