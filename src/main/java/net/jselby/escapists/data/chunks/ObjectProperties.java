package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The various properties of an object.
 */
public class ObjectProperties extends Chunk {
    @Override
    public void init(ByteReader buffer, int length) {
        // TODO: This needs an objectType to successfully read
        /**
         * self.objectType = objectType
         reader = self._loadReader
         del self._loadReader
         reader.seek(0)

         self.isCommon = False
         if objectType == QUICKBACKDROP:
         self.loader = self.new(QuickBackdrop, reader)
         elif objectType == BACKDROP:
         self.loader = self.new(Backdrop, reader)
         else:
         self.isCommon = True
         self.loader = self.new(ObjectCommon, reader)

         */
    }
}
