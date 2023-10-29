import React, { useState } from 'react';
import PropTypes from 'prop-types';

import { groupType, fieldType } from './formTypes';
import { Dialog } from '..';
import FormBase from './FormBase';

const Form = props => {
    const { fields, groups, canceled, confirmed } = props;
    const [valid, setValid] = useState(false);

    return (
        <Dialog close={canceled} confirmed={confirmed} disabled={!valid}>
            <FormBase fields={fields} groups={groups} setValid={setValid} />
        </Dialog>
    );
};

Form.propTypes = {
    fields: PropTypes.arrayOf(fieldType),
    groups: PropTypes.arrayOf(groupType),
    canceled: PropTypes.func,
    confirmed: PropTypes.func,
};

Form.defaultProps = {
    fields: [],
    groups: [],
    canceled: () => {},
    confirmed: () => {},
};

export default Form;
