package glenn.gases;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockGasPipe extends Block
{
	/**
	 * The gas block contained by this gas pipe.
	 */
	public BlockGas containedGas;
	
	/**
	 * Constructs a new gas pipe block. Because of technical reasons, each type of gas needs its own gas pipe block for pipe usage.
	 * @param par1 - Block ID
	 * @param containedGas - The gas this pipe will carry
	 */
	public BlockGasPipe(int par1, BlockGas containedGas)
	{
		super(par1, Material.circuits);
		this.containedGas = containedGas;
		
		this.setHardness(1.0F);
	}
}