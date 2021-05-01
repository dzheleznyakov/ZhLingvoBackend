import React from 'react';
import PropTypes from 'prop-types';

import classes from './WordMainForm.module.scss';

import OnHoverEditable from '../Editable/OnHoverEditable';
import { MAIN_FORM } from '../../../../../../static/constants/wordEditModalTypes';

const WordMainForm = props => {
    const { children: mainForm } = props;

    return (
        <OnHoverEditable
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
