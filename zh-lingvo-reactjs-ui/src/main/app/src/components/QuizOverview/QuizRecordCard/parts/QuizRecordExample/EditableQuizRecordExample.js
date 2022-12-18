import React from 'react';
import PropTypes from 'prop-types';
import { quizRecordExampleType } from '../../types';
import NewPartButton from '../Editable/NewPartButton/NewPartButton';
import { EXAMPLE_DELETE, EXAMPLE_EDIT, EXAMPLE_NEW } from '../../../../../static/constants/quizRecordEditModalTypes';
import OnHoverEditable from '../Editable/OnHoverEditable';
import QuizRecordExample from './QuizRecordExample';

export const NULL_EXAMPLE = { id: -1, expression: '', explanation: '' };

const EditableQuizRecordExample = props => {
    const { entry: example, path } = props;

    if (example === NULL_EXAMPLE)
        return (
            <NewPartButton
                label='example'
                modalType={EXAMPLE_NEW}
                path={path}
                block
            />
        );

    return (
        <OnHoverEditable
            editModalType={EXAMPLE_EDIT}
            deleteModalType={EXAMPLE_DELETE}
            path={path}
            block
        >
            <QuizRecordExample entry={example} />
        </OnHoverEditable>
    );
};

EditableQuizRecordExample.propTypes = {
    entry: quizRecordExampleType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string),
};

EditableQuizRecordExample.defaultProps = {
    path: [],
};

export default EditableQuizRecordExample;
