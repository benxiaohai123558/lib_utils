package com.lib.utils.android.image

import java.io.InputStream

/**
 *
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
interface IInputStreamCreator {

    fun onCreateInputStream(): InputStream?

}